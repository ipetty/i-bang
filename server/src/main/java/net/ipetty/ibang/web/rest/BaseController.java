package net.ipetty.ibang.web.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 基础Controller类，提供基础功能，如Logger
 * 
 * @author luocanfeng
 * @date 2014年3月27日
 */
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public abstract class BaseController {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

}
