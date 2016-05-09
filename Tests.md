#Tests Done (Automated)
##Unit Test 

- FiboTaskStoreRedisTest
  - testSaveAndQuery: Save and query and update task. Make sure task store works well. 
- FiboSequenceGeneratorRedisTest
  - testGenerateFiboWithoutCache: Generate a fibo sequence and verify if all data stored in cache and max cached sn is updated correctly
  - testGenerateFiboAlreadyCached: Data already in cache, max cached sn should be old SN.

##Functional Test

- FiboResourceTest
  - testGetFibo: Get a fibo sequence with number 1000 or 100,000. Make sure each value is correct.
  - testGetBatchFibo: Send 10 requests with different number and make sure each value is correct.
  - testNegativeSn: Input a negative number or not a number make sure get a appropriate error message.

#More Tests
Besides the basic functional testing, the following test areas are worthy to think about:

- Max number system supports
- 
The max number tested is 100,000. Not sure if max integer works which depends on how much memory a host has.  

- Failure Handling Test
Even each service only has 1 instance running, when executor service is down, no task loss or handled multiple times.  

- Load Testing

Assuming each service only has 1 instance, make 100 or 200 concurrent users keep loading the system with varrious number. Expect each request is returned in accepted duration and no error happened. On the host where fetch service runs, the CPU usage should be < 50%.

- Performance Testing

Similar to load, running in shorter time and more care about the performance result, specially the one changes as conccurent user number changes.

- Scalability Testing

When increasing the number of executor and fetch service, the performance should be better.

