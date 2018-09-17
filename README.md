
# 1.Overview

Designed to run specific code with a timer, which is a self-organising server with loose coupling and high avaiability.

![delay-callback](https://user-images.githubusercontent.com/1615053/45608444-715ae880-ba85-11e8-8484-22494f1259a2.png)

# 2.delay-callback-server
The server persists the registered callback data, ensuring that the callback can always be invoked with retry strategy, and it's stateless, which means it can be scaled out easily.

## Dependencies
[dubbo](http://dubbo.apache.org/en-us)/spring cloud/zookeeper/redis

## deploy
```
cd delay-callback-server
mvn spring-boot:run
```

# 3.delay-callback-client
The client encapsulates callback server discovery and callback client registry, and provides a helper to easily write registration and callback code. See **client-demo**.

## Dependencies
dubbo/spring

# 4.client-demo
The demo shows how to use delay-callback-client to register callback and write callback logic.
## a.Add dependency
```
        <dependency>
            <groupId>com.johnli</groupId>
            <artifactId>delay-callback-client</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```
## b.Write java code
```
        // params that will be delivered while callback
        List<String> params = new ArrayList<>();
        params.add("hello");
        params.add("world");

        // do register
        RegisterResult register = DelayCallbackHelper.register(new CallbackParam(params, 10), new DelayCallback() {
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

# 5.License
Under the Apache 2.0 license.
