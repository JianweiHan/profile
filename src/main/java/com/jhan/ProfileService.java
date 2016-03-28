package com.jhan;

/**
 * Created by jhan on 3/27/16.
 */
public interface ProfileService {
    public Profile findById(String id);
    public Profile update(Profile profile);
    public Profile create(Profile profile);
    public Profile delete(String id);
}
