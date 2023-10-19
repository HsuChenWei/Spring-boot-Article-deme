package com.test123.demo.service.utils;

import com.test123.demo.model.Security.TokenPair;
import io.vavr.control.Option;

public interface UtilService {

    Option<TokenPair> generateTokenPair(String id);
}
