package com.quantiply.samza;

import com.codahale.metrics.*;
import org.apache.samza.metrics.MetricsRegistry;

public class MetricAdaptor {
    private final MetricRegistry codaRegistry;
    private final MetricsRegistry samzaRegistry;
    private final String groupName;


    public MetricAdaptor(MetricRegistry codaRegistry, MetricsRegistry samzaRegistry, String groupName) {
        this.codaRegistry = codaRegistry;
        this.samzaRegistry = samzaRegistry;
        this.groupName = groupName;
    }

    public <T extends Metric> T register(String name, T metric) {
        codaRegistry.register(name, metric);
        return registerWithSamza(name, metric);
    }

    public Histogram histogram(String name) {
        Histogram h = codaRegistry.histogram(name);
        return registerWithSamza(name, h);
    }

    public Counter counter(String name) {
        Counter c = codaRegistry.counter(name);
        return registerWithSamza(name, c);
    }

    public Timer timer(String name) {
        Timer t = codaRegistry.timer(name);
        return registerWithSamza(name, t);
    }

    public Meter meter(String name) {
        Meter m = codaRegistry.meter(name);
        return registerWithSamza(name, m);
    }

    public Gauge gauge(String name, Gauge g) {
        return registerWithSamza(name, g);
    }

    private <T extends Metric> T registerWithSamza(String name, T metric) {
        samzaRegistry.newGauge(groupName, name, new MapGauge(name, metric));
        return metric;
    }

}
