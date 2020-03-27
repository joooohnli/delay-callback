# [中文文档](https://joooohn.com/timer/callback/2018/09/21/delay_callback.html)

# 1.Overview

Designed to run specific code with a timer, which is a self-organising server with loose coupling and high avaiability.

![delay-callback](https://joooohnli.github.io/images/delay-callback.png)
## Features

- Your application registers a callback whith exact trigger time that the callback server will then invoke.
- Your application could cancel a callback before it's invocation arrival.
- The callback server confirms callback invocation if that callback has retry strategy being set. 
- The callback server is born with HA, you only need to deploy multiple instances.
- Callback trigger time is accurate to second.
- Registration can be idempotent. (while idempotent of callback invocation should be consideration of your application.)
- Callbacks are grouped by your application name.
- Easy to use, just like writing local java callbacks.

# 2.Projects
## delay-callback-server
The callback server persists registered callbacks (to `redis`), ensuring that callbacks will always be invoked. Besides, the callback server is stateless, so out-of-the-box, it can be scaled out easily.

### Main components
- **Facade**: register && unRegister
- **ScanJob**: scheduled job scans available callback tasks. Master election will be made automatically if you have more than one instance deployed.
- **Distributor**: distributes callback tasks based on dubbo's loadbanlance that can be customed by configuration. 
- **Processor**: processes callbacks under protection of distributed lock.


### Dependencies
[dubbo](http://dubbo.apache.org/en-us)/spring cloud/zookeeper/redis

## delay-callback-client
The callback client encapsulates communication detail between your application and the callback server, and provides a helper to easily write registration and callback code.

### Main components
- **Initializer**: initializes local callback implementation 
- **Registry**: registers callback client to zookeeper
- **Refer**: discovers callabck server from zookeeper
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
Shows how to use delay-callback-client to register callback and write callback logic.

# 3.Get started
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

# 4.Advanced usages
// todo

# 5.License
delay-callback is under the Apache 2.0 license. See the [LICENSE](https://github.com/joooohnli/delay-callback/blob/master/LICENSE) file for details.
