package com.yondql.springbootconfig;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Genconfig {

    public static void main(String[] args) throws Exception {
        writeConfig("ydsjyzmch");
        writeConfig("ydsjyzmru");
    }

    static void writeConfig(String config) {
        String k = ResourceUtil.readUtf8Str("local/key.local");
        String p = ResourceUtil.readUtf8Str("local/pkey.local");
        byte[] kb = Base64.getDecoder().decode(k);
        byte[] pb = Base64.getDecoder().decode(p);
        RSA rsa = SecureUtil.rsa(kb, pb);
        String json = ResourceUtil.readUtf8Str(StrUtil.format("local/{}.json", config));
        String ejson = rsa.encryptBase64(json, KeyType.PublicKey);

        File file = FileUtil.writeBytes(ejson.getBytes(StandardCharsets.UTF_8), StrUtil.format("{}/{}.txt", System.getProperty("user.dir"), config));
        System.out.println(file.getAbsoluteFile());

        System.out.println(ejson);
        byte[] dbytes = rsa.decrypt(Base64.getDecoder().decode(ejson), KeyType.PrivateKey);
        System.out.println(new String(dbytes));
    }

}
