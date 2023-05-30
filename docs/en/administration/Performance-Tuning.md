---
layout: global
title: Performance Tuning
nickname: Performance Tuning
group: Administration
priority: 4
---

* Table of Contents
{:toc}

This document goes over various tips and configurations to tune Alluxio performance.

## Common Performance Issues

The following is a checklist to run through to address common problems when tuning performance:

1. Are there frequent JVM GC events?

   Frequent and long GC operations on master or worker JVMs drastically slow down the process.
   This can be identified by adding logging for GC events; append the following to `conf/allulxio-env.sh`:

```
ALLUXIO_JAVA_OPTS=" -XX:+PrintGCDetails -XX:+PrintTenuringDistribution -XX:+PrintGCTimeStamps"
```

   Restart the Alluxio servers and check the output in `${ALLUXIO_HOME}/logs/master.out` or
   `${ALLUXIO_HOME}/logs/worker.out` for masters and workers respectively.

Also check out the [metrics system][2] for better insight into how the Alluxio service is performing.

[1]: {{ '/en/api/Java-API.html' | relativize_url }}#location-policy
[2]: {{ '/en/operation/Metrics-System.html' | relativize_url }}

## General Tuning

### JVM Monitoring

To detect long GC pauses, Alluxio administrators can set `alluxio.master.jvm.monitor.enabled=true`
for masters or `alluxio.worker.jvm.monitor.enabled=true` for workers.
They are enabled by default in Alluxio 2.4.0 and newer.
This will trigger a monitoring thread that periodically measures the delay between two GC pauses.
A long delay could indicate that the process is spending significant time garbage collecting,
or performing other JVM safepoint operations.
The following parameters tune the behavior of the monitor thread:

<table class="table table-striped">
<tr><th>Property</th><th>Default</th><th>Description</th></tr>
<tr>
  <td>alluxio.jvm.monitor.warn.threshold</td>
  <td>10sec</td>
  <td>Delay required to log at WARN level</td>
</tr>
<tr>
  <td>alluxio.jvm.monitor.info.threshold</td>
  <td>1sec</td>
  <td>Delay required to log at INFO level</td>
</tr>
<tr>
  <td>alluxio.jvm.monitor.sleep.interval</td>
  <td>1sec</td>
  <td>The time for the JVM monitor thread to sleep</td>
</tr>
</table>

## Client Tuning

### OOM of Alluxio processes
1. Alluxio process can get killed by system OOM killer and die silently
 * Check `dmesg -T | egrep -i 'killed process'`
 * This will show which process (if any) got killed by OOM killer
If confirmed OOM issue, start by increasing xmx, directmemory setting of the relevant process
 * Sometimes the log will show an Out Of Memory exception, this is a Java reported OOM. 
 * This is typically caused by not enough system resources, such as ulimit, thread stack space etc.  
