package com.sha.camel.camelroutes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CamelRoutesApplication extends RouteBuilder{

	public static void main(String[] args) {
		SpringApplication.run(CamelRoutesApplication.class, args);
	}

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		from("timer:sdfs")
		.log("log:sdfs");
	}

}
