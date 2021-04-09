package eu._5gzorro.legalproserepository.utils;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidSourceImpl implements UuidSource {

    public UuidSourceImpl()  {
    }

    public UUID newUUID() {
        return UUID.randomUUID();
    }

}
