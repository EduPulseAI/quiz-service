package xyz.catuns.edupulse.quiz.config.properties;

import lombok.Data;

@Data
public class AsyncProperties {
    /*
     * Async Core Pool Size
     */
    private int corePoolSize;
    /*
     * Async Max Pool Size
     */
    private int maxPoolSize;
    /*
     * Async Queue Capacity
     */
    private int queueCapacity;
    /*
     * Async Thread Name Prefix
     */
    private String threadNamePrefix;
}
