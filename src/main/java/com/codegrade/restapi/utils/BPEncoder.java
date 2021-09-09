package com.codegrade.restapi.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BPEncoder extends BCryptPasswordEncoder {

}
