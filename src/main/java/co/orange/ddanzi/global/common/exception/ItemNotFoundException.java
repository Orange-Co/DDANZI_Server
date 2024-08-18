package co.orange.ddanzi.global.common.exception;

import co.orange.ddanzi.global.common.error.Error;

public class ItemNotFoundException  extends ApiException{
    public ItemNotFoundException() {
        super(Error.ITEM_NOT_FOUND, Error.ITEM_NOT_FOUND.getMessage());
    }
}