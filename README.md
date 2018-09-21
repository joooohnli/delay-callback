
# 1.Overview

Designed to run specific code with a timer, which is a self-organising server with loose coupling and high avaiability.

![delay-callback](https://joooohnli.github.io/images/delay-callback.png)
## Features

- The application registers callbacks and sets the execution time, which the server will invoked on arrival time.
- The application can cancel a callback task before it's been invoked.
- The server makes sure that a callback will be executed if it's retry strategy is properly seted during registration. 
- The server ensures HA, you only need to deploy multiple instances.
- Registration can be idempotent.
- Idempotent of callback should be guaranteed by your application.
- Easy to use, just like writing local java callback.

# 2.Projects
## delay-callback-server
The server persists the registered callback data, ensuring that the callback can always be invoked with retry strategy. And it's stateless, so out-of-the-box, it can be scaled out easily.

### Main components
- **Facade**: register && unRegister
- **ScanJob**: scheduled job scans available callback tasks. Master election will be made automatically if you have more than one instance deployed.
- **Distributor**: distributes callback tasks based on dubbo's loadbanlance that can be customed by configuration. 
- **Processor**: processes callback tasks under protection of distributed lock.


### Dependencies
[dubbo](http://dubbo.apache.org/en-us)/spring cloud/zookeeper/redis

## delay-callback-client
The client encapsulates communication detail between your application and the callback server, and provides a helper to easily write registration and callback code.

### Main components
- **Initializer**: init local callback implementation 
- **Registry**: register callback client to zookeeper
- **Refer**: discover callabck server from zookeeper
- **Helper**: ```DelayCallbackHelper```, the main class you will interact with:
```java
public class DelayCallbackHelper {
    /**
     * register callback
     *
     * @param callbackParam params
     * @param delayCallback implementation of callback logic
     * @return unique id
     */
    public static RegisterResult register(CallbackParam callbackParam, DelayCallback delayCallback) {...}

    /**
     * cancel registration
     *
     * @param uid unique id
     * @return result
     */
    public static UnRegisterResult unRegister(String uid) {...}
}

```

### Dependencies
dubbo/spring

## delay-callback-interface
common interfaces and objects used by client and server

## client-demo
The demo shows how to use delay-callback-client to register callback and write callback logic.

# 3.Getting started
### i.Start server
```bash
cd delay-callback-server
# edit application.properties
mvn spring-boot:run
```

### ii.Add dependency to your application
```xml
        <dependency>
            <groupId>com.johnli</groupId>
            <artifactId>delay-callback-client</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```
### iii.Write java code in your application
```java
        // params that will be delivered while callback
        List<String> params = new ArrayList<>();
        params.add("hello");
        params.add("world");

        // will callback in 3 seconds
        int delaySeconds = 3;
        
        // do register
        RegisterResult register = DelayCallbackHelper.register(new CallbackParam(params, delaySeconds), new DelayCallback() {
            @Override
            public String alias() {
                return "callback01";
            }

            @Override
            public boolean onCallback(CallbackRequest request) {
                // do whatever you want here

                return true;
            }
        });

        if (register.isSuccess()) {
            System.out.println("register successfully");

            // you can cancel before the callback being invoked.
            //DelayCallbackHelper.unRegister(register.getUid());
        }
```
You see, just like writing local java callback.

### iv.run your application

# 4.Advanced usage
// todo

# 5.License
Apache Dubbo is under the Apache 2.0 license. See the [LICENSE](https://github.com/joooohnli/delay-callback/blob/master/LICENSE) file for details.
