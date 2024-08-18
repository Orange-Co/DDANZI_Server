package co.orange.ddanzi.common.exception;

import co.orange.ddanzi.common.error.Error;

public class ItemNotFoundException  extends ApiException{
    public ItemNotFoundException() {
        super(Error.ITEM_NOT_FOUND, Error.ITEM_NOT_FOUND.getMessage());
    }
}