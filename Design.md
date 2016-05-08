#Requriements Analysis and High Level Design

The basic function is very simple, nothing much wrothy to discuss. This kind of task like computing Fibonacci sequence has 2 features: 1) Take very high CPU usage and 2) Takes huge space. Based on the 2 features the architecture and system design uses the following ways to meet address the following questions:

1. Streaming response: The returning fibonacci sequence might be huge, so the service returns response in streamming way.
- Cache: Tests found that computing without cache takes very high CPU usage. Single request is okay but multiple requests lead to 100% CPU usage. With the help of cache system, when processing multiple tasks, System is able to keep a stable CPU usage. Response time depends on the network bandwidth. 
- Async API: Cache can help to solve the CPU usage issue, however it might be hard to make sure everything is in cache. There are still big chance for Sync API to overload system. So the safe way is design an async system. End user should be able to expect this is a time consuming task and don't expect much on latency. If there are VIP kind of customers really want sync service, it is not hard to add. That would use dedicated resources to take task.
- Distributed Architecture: based on the analysic above, we could have various requirements on hardware reousrce. Processor nodes need more CPU, Cache system need more memory and Fetch nodes need ore bandwidith. So running services on different hosts is good to take advantage of resources. Meanwhile, it provides cabability to scale to large.

# Archtecture
Ths system consists of 5 services: FiboService, FiboExecutorService, FiboFetchService, FiboTaskStore and FiboSequenceStore
- FiboService is used to accpet user's requests and then create tasks and submit to Executor Service.
  -- Kafka is used as task queue to make sure: 
     1. Task should NOT be lost.
     2. Capable of distributing load to multiple Executor nodes and also handle network or server down issue.  
     
- FiboExecutorService is the task processor. 

  It has a task queue in memory containing all tasks information. By which FiboService can submit and query task. 

  FiboTaskWorker in FiboExecutorService is a thread running periodically to retrieve tasks in time order and then execute it. For each fibonacci task, if there is one with same sequence number 
  in FiboSequenceStore, the task can be marked as ready directly. If not, processsor will compute one and store that into the store for future use.

- FiboSequenceStore
The repository to store all fibonacci sequence. Now all sequences are store in local disk as separate files. FiboExecutorService can store and 
retrieve sequences by sequence number.

#API
Expose 2 public REST APIs. 

- GET myapp/fibo?sn=(number)

    This returns task id directly.

- GET myapp/fibo/task/id=(task id)

    The first byte is to indicate the status. If it's 0 (means ready) then continue to read rest of it.

# Failure Handling

#More Things to Be Optimized
1. Right now all fibonacci sequence files in store are stored in flat way. It's required to bulid a tree hierarchy for better performance.
2. Each task should have expired date. It will be removed after that. 
3. Persist task queue in mysql or cassandra so the task data will not be lost when system reboot 
