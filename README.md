
# Overview

Designed to run specific code with a timer, the timer is a self orgnised server with loose coupling and high avaiability 

![delay-callback](https://user-images.githubusercontent.com/1615053/45608444-715ae880-ba85-11e8-8484-22494f1259a2.png)

# delay-callback-server
The server persists the registered callback data, ensuring that the callback can always be invoked with retry strategy, and it's stateless, which means it can be scaled out easily.

dependencies:dubbo/spring cloud/zookeeper/redis

# delay-callback-client
The client encapsulates callback server discovery and callback client registry, and provides helper to easily write registration and callback code. see **client-demo**.

dependencies:dubbo/spring

# client-demo
show how to use delay-callback-client to register callback and write callback logic.
