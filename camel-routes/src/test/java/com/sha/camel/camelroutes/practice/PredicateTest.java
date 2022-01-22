package com.sha.camel.camelroutes.practice;

import org.apache.camel.Predicate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

public class PredicateTest extends CamelTestSupport{

	protected RoutesBuilder createRouteBuilder() {
		return new RouteBuilder() {

			Predicate pred = exchangeProperty("sfs").isEqualTo("sdf");
			
			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				from("direct:start").routeId("route1")
			    .choice().id("choiceId")
			       .when(pred)  //pred is some predicate
			          .to("log:choiceTrue")
			        //.to(<some other route>)
			    .otherwise()
			          .to("log:choiceFalse")
			        //.to(<some other route>)
			    .end()
			    .end();
				
				
				from("direct:diversion")
				.to("log:divertedRoute")
				.choice()
					.when().simple("'abc' != 'abc'") //true condition
						.to("log:TrueDiversion")
						//.to(<some other route>)
				.otherwise()
						.to("log:FalseDiversion")
						//.to(<some other route>)
				.end()
				.end();
			}
			
		};
	}
	
	@Test
	public void testMocksAreValid() throws Exception{
		
		RouteDefinition routeDef = context.getRouteDefinition("route1");
		
		AdviceWith.adviceWith(routeDef, context,
				new AdviceWithRouteBuilder() {
					
					@Override
					public void configure() throws Exception {
						// TODO Auto-generated method stub
						weaveById("choiceId").replace().to("direct:diversion");
						weaveAddLast().to("mock:mockendpoint");
					}
				});
		
		context.start();
		template.requestBody("direct:start", "sampleBody");
		
		MockEndpoint mock = getMockEndpoint("mock:mockendpoint");
		mock.expectedMessageCount(1);
		
		mock.assertIsSatisfied();
		
	}
}
