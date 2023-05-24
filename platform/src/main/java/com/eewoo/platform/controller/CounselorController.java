package com.eewoo.platform.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasAuthority('c')")
@RestController
@RequestMapping("/counselor")
public class CounselorController {
}
