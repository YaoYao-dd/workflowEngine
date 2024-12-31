package com.yaod.workflow.engine.core.exception;

/**
 * @author Yaod
 **/
public class ItemNotFoundExecption extends RuntimeException{
    private final String itemId;

    public static ItemNotFoundExecption raiseNotFoundItemExec(String itemId){
        return new ItemNotFoundExecption(itemId, "item is not found.");
    }
    public ItemNotFoundExecption(String itemId, String message) {
        super(message);
        this.itemId = itemId;
    }

    public ItemNotFoundExecption(String itemId, String message, Throwable cause) {
        super(message, cause);
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }
}
