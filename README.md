
# 1.Overview

Designed to run specific code with a timer, which is a self-organising server with loose coupling and high avaiability.

![delay-callback](https://user-images.githubusercontent.com/1615053/45608444-715ae880-ba85-11e8-8484-22494f1259a2.png)

# 2.Projects
## delay-callback-server
The server persists the registered callback data, ensuring that the callback can always be invoked with retry strategy, and it's stateless, which means it can be scaled out easily.


### Structure
- **Facade**: register && unRegister
- **ScanJob**: scheduled job scans available callback tasks. Master election will be made automatically if has more than one instance deployed.
- **Distributor**: distributes callback tasks based on dubbo's loadbanlance that can be customed by configuration. 
- **Processor**: processes callback tasks with distributed lock's protection.


### Dependencies
[dubbo](http://dubbo.apache.org/en-us)/spring cloud/zookeeper/redis

## delay-callback-client
The client encapsulates callback server discovery and callback client registry, and provides a helper to easily write registration and callback code.

### Structure

- **Initializer**: init local callback 
- **Registry**: register callback client to zookeeper
- **Refer**: discover callabck server from zookeeper
- **Helper**: ```DelayCallbackHelper```, the main class you will interact with:
```java
public class DelayCallbackHelper {
    /**
     * @param callbackParam params
     * @param delayCallback implementation of callback logic
     * @return unique id
     */
    public static RegisterResult register(CallbackParam callbackParam, DelayCallback delayCallback) {...}

    /**
     * cancel registration
     *
     * @param uid unique id
     * @return
     */
    public static UnRegisterResult unRegister(String uid) {...}
}

```

### Dependencies
dubbo/spring

## delay-callback-interface
common interfaces and objects of client and server

## client-demo
The demo shows how to use delay-callback-client to register callback and write callback logic.

# 3.Getting started
### i.Start server
```
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
Under the Apache 2.0 license.
