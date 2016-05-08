#Tests Done (Automated)
##Unit Test 

- FiboTaskStoreRedisTest
  - testSaveAndQuery: Save and query and update task. Make sure task store works well. 
- FiboSequenceGeneratorRedisTest
  - testGenerateFiboWithoutCache: Generate a fibo sequence and verify if all data stored in cache and max cached sn is updated correctly
  - testGenerateFiboAlreadyCached: Data already in cache, max cached sn should be old SN.

##Functional Test

- FiboResourceTest
  - testGetFibo
  - testGetBatchFibo
  - testNegativeSn

#More Tests
Besides the basic functional testing, the following test areas are worthy to think about:

- Max number system support

It's impossible for system to support unlimited value. We should have one value from customer and see
if system can hold it and transfer it over network. 

- Load Testing

Normal concurrent users keep loading the system with varrious number. Expect each request is returned in accepted duration and no error happened.

- Performance Testing

Similar to load, running in shorter time and more care about the performance result, specially the one changes as conccurent user number changes.

- Scalability Testing

When increasing servers, system can serve more users in accepted response time.

- Failure Handling Test

When part of node down or network issue happens, system can still continue service. 
