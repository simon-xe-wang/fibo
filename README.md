# FiboService
FiboService is REST based web service where user is able to get Fibonacci sequence by inputting a number. 

FiboService uses distributed architecture and async task-based API as computing Fibonacci sequence is both time consuming and space consuming. More details please see the Design document listed bottom.

Here is the typical flow to get a Fibonacci sequence:

1. Submit a request and get a task id.
2. Query task status by reading the first byte of the response.
3. If task not ready, keep polling the task status.
4. If task ready, continue reading the rest of the response as a stream.

Note in step 4, to avoid out of memory issue on client side, please use streaming way to read response as a Fibonacci sequence might be huge. For example, inputing 100K result size will reach ~1.7GB. 

This is a java example, using Jersey Client, to show the above procedure:
```
    public void getFiboAsyncAsFile(int sn, String resultFilePath) throws Exception {

        // Build a Rest request and send to server
        Client c = ClientBuilder.newClient();
        rootTarget = c.target("URL with server, port and service URI");
        WebTarget fibTarget = rootTarget.path("fibo").queryParam("sn", sn);
        Invocation.Builder invocationBuilder = fibTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        // Process http status
        int httpStatus = response.getStatus();
        if (httpStatus < 200 || httpStatus >= 300) {
            throw new Exception("Request failed. Status code is " + httpStatus);
        }

        // Get task id
        FiboTask task = response.readEntity(FiboTask.class);
        System.out.println("Get a task. Task id is " + task.getId());

        // Fetch Fibo Sequence by task id
        WebTarget fiboTaskTarget = rootTarget.path("fibo/task").queryParam("id", task.getId());
        invocationBuilder = fiboTaskTarget.request(MediaType.APPLICATION_OCTET_STREAM);

        while (true) {
            response = invocationBuilder.get();

            httpStatus = response.getStatus();
            if (httpStatus < 200 || httpStatus >= 300) {
                throw new Exception("Request failed. Status code is " + httpStatus);
            }

            InputStream rspStream = response.readEntity(InputStream.class);
            try {
                // Process task status by reading the first byte
                int taskStatus = rspStream.read();
                if (taskStatus != FiboTask.STATE_READY) { // Not ready
                    System.out.println("Task not ready, waiting 10 seconds and retry...");
                    Thread.sleep(10*1000);
                    continue;
                } else { // Task ready. Streaming read and write to file
                    FileOutputStream rtResultFile = null;
                    try {
                        rtResultFile = TestUtil.createResultFileOutputStream(resultFilePath);
                        byte[] buf = new byte[FILE_READ_BUF_SIZE];
                        int nRead = 0;
                        while ((nRead = rspStream.read(buf)) > 0) {
                            rtResultFile.write(buf, 0, nRead);
                        }
                    } finally {
                        rtResultFile.close();
                    }
                    break;
                }
            } finally {
                rspStream.close();
            }
        }
    }
```

- [Build and Deployment Service](https://github.com/thinkslower/fibo/blob/master/BuildAndDeployment.md)
- [Design](https://github.com/thinkslower/fibo/blob/master/Design.md)
- [Tests](https://github.com/thinkslower/fibo/blob/master/Tests.md)
- [Next](https://github.com/thinkslower/fibo/blob/master/Next.md)

