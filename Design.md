#Requriements
The basic function is very simple, nothing much wrothy to discuss. Because Fibonacci value computing is very time consuming operation, and the sequence 
takes huge size, the follwoing questions are needed to consider:

1. Streaming response: The returning fibonacci sequence might be huge, so the service returns response in streamming way.
2. Async API: Computing a fibonacci value could take very long time, so asynchronus, task based API is required. This way system accepts requests very quick and
processes all requests in controlled way.
3. The number range can handle: This solution stores sequence in local disk for future use. A large number needs very huge storage space. 
I have no exact size about how much disk space is required for a number. It could be at TB or PB level.
4. Performance: Testing found that most time is spent on math computation and memory operation. Caching fibonacci sequence can improve peroformance 
very much. For example, Computing a fibonacci for 40000 takes 15 seconds in my laptop, but takes 1 second only if load data directly from a local file. 
5. Scalability: Each service is stateless, so it's easy to increase servers to handle more users. But this 
needs a separated Fibonacci storage service which is able to be shared by front service nodes. Will discuss the solution in More Ideas
6. Monitoring and Security: This are definily needed in production environment but not implemented in this solutioin due to time limit.

# Archtecture
Ths system consists of 3 components: FiboService, FiboExecutorService and FiboSequenceStore
- FiboService is used to accpet user's requests. 2 features:

  1. Submit request to FiboExecutorService. If the request is accepted successfully, it will return a FiboTask to client.
  2. Accept task query request and forward to FiboExecutorService.

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
