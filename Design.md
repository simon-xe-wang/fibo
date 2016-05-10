#Requriements Analysis and High Level Design

The basic function is very simple, nothing much wrothy to discuss. This kind of task like computing Fibonacci sequence has 2 features: 1) Take very high CPU usage and 2) Takes huge space. Based on the 2 features the architecture and system design uses the following ways to meet address the following questions:

1. Streaming response: The returning fibonacci sequence might be huge, so the service returns response in streamming way.
- Cache: Tests found that computing without cache takes very high CPU usage. Single request is okay but multiple requests lead to 100% CPU usage. With the help of cache system, when processing multiple tasks, System is able to keep a stable CPU usage. Response time depends on the network bandwidth. 
- Async API: Cache can help to solve the CPU usage issue, however it might be hard to make sure everything is in cache. There are still big chance for Sync API to overload system. So the safe way is design an async system. End user should be able to expect this is a time consuming task and don't expect much on latency. If there are VIP kind of customers really want sync service, it is not hard to add. That would use dedicated resources to take task.
- Distributed Architecture: based on the analysic above, we could have various requirements on hardware reousrce. Processor nodes need more CPU, Cache system need more memory and Fetch nodes need ore bandwidith. So running services on different hosts is good to take advantage of resources. Meanwhile, it provides cabability to scale to large.

# Archtecture
See [Diagram](https://drive.google.com/file/d/0B4dYAL73RDJHbjRnb1c0MkZ5a2s/view?usp=sharing)

Ths system consists of 5 services: FiboService, FiboExecutorService, FiboFetchService, FiboTaskStore and FiboSequenceStore
- FiboService is used to accpet user's requests and do:
  1. Create task and store to Task Store
  2. Submit to Executor Service through Kafka-based Task Queue.
  
    - Kafka is used as it provides 2 capabilities:
      1. No task loss.
      2. Capable of distributing load to multiple Executor nodes and also handle network or server down issue.  
     
- FiboExecutorService is the task processor. 

  Task Consumer polls tasks from the Queue, for each task:
    1. if the sequence is alrady in Fibo Store, then mark the task as ready directly. 
    2. if whole or part of sequence is not in, then compute and store it into the store.  
    
- FiboTaskStore and FiboSequenceStore

The repository to store all fibonacci sequences and tasks. Use Redis as the underlying storage. It should be splitted to 2 Redis instances. 

#API
Expose 2 public REST APIs:

- GET myapp/fibo?sn=(number)

    This returns task id directly.

- GET myapp/fibotask/id=(task id)

    The first byte is to indicate the status. If it's 0 (means ready) then continue to read rest of it.

#Next
This system is able to extend to large scale to get better performance and high availability. Below are things we can do next:
- Use Nginx as load balancer and multiple service nodes. 
- Setup Redis cluster to persist Fibonacci values. All values could be partitioned to different groups, and each group has one master and multiple slaves for failover. 
- Deploy multiple executor nodes to receive and handle tasks. We can create multiple partitions on the task topic, each executor node subscribe one or more partitions. Also multiple nodes can form a kafka consumer group for node down or network issue.
