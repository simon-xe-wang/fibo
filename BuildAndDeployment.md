This solution adopts distibuted architecture and there are about 5-6 services required. So building and deployment are tough job. 
I realized that dockerization solution by putting each service into a docker container should be much better if not the best. 
But due to time limit and I have trouble with accessing to docker hub so still have to use the traditional way.  

Here just shows the steps to deploy and run everything within single host, although the who system should be able to run across multiple hosts.

Environment Requirements:
  - OS: Linux only. (Tested on SLES12 but other distribution should be fine).
  - Java: 1.8
  - Apache Maven 3.2.1: required to compile and build jars. Make sure the path to bin/ is added to $PATH environment.

### Build
##### Step 1: Download the code from github 
```
git clone "https://github.com/thinkslower/fibo.git"
```
##### Step 2: Build and package FiboService
```
# cd fibo
# ./build.sh
```
If everything is fine, you will find the fiboservice-1.0.zip under fibo directory

### Run
Before jumping into long steps, I would like to show you which services are reqired to start:

1. Redis
- Zookeeper
- Kafka broker
- FiboExecutor Service
- FiboService

####Steps:
##### Step 1: Build and Start Redis
- Build
```
wget http://download.redis.io/redis-stable.tar.gz
tar xvzf redis-stable.tar.gz
cd redis-stable
make
```
- Run
```
$ redis-server
[28550] 01 Aug 19:29:28 # Warning: no config file specified, using the default config. In order to specify a config file use 'redis-server /path/to/redis.conf'
[28550] 01 Aug 19:29:28 * Server started, Redis version 2.2.12
[28550] 01 Aug 19:29:28 * The server is now ready to accept connections on port 6379
... more logs ...
```
Any trouble please refer to http://redis.io/topics/quickstart

##### Step 1: Start Zookeeper & Kafka
##### Step 1: Start Fibo Exectutor
##### Step 1: Start Fibo Service
##### Step: Run Fibo Client
```
# cd bin/
# ./getfibo.sh <number> <file path> 
```

Unpack the package to a directory
- Step 4: Configure Service

  Set the host, port of service you want to run.
  
- Step 3: Run Service
```
# cd bin/
# ./startserver

