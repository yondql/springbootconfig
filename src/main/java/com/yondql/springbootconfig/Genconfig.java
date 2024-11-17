package com.yondql.springbootconfig;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Genconfig {

    public static void main(String[] args) throws Exception {
        writeConfig("ydsjyzm");
    }

    static void writeConfig(String config) {
        String k = ResourceUtil.readUtf8Str("local/key.local");
        String p = ResourceUtil.readUtf8Str("local/pkey.local");
        byte[] kb = Base64.getDecoder().decode(k);
        byte[] pb = Base64.getDecoder().decode(p);
        RSA rsa = SecureUtil.rsa(kb, pb);
        String json = ResourceUtil.readUtf8Str(StrUtil.format("local/{}.json", config));
        String ejson = rsa.encryptBase64(json, KeyType.PublicKey);
        JSONObject jo = new JSONObject();
        jo.set("code", 0);
        jo.set("data", ejson);
        File file = FileUtil.writeBytes(jo.toStringPretty().getBytes(StandardCharsets.UTF_8), StrUtil.format("{}/{}.txt", System.getProperty("user.dir"), config));
        System.out.println("update -> " + file.getAbsoluteFile());
    }


    static void genKeyPair() {
        //
//        KeyPair keyPair = SecureUtil.generateKeyPair("RSA", 2048);
//
//        System.out.println(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
//        System.out.println(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
    }

}
