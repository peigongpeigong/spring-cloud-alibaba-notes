package com.peigong.springcloudalibaba;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: lilei
 * @create: 2020-07-24 15:06
 **/
@Component
public class SentinelSampleWithAnnotation {

    @SentinelResource(value = "hello", blockHandler = "blockHandler")
    public String hello(){
        System.out.println("hello");
        return "hello";
    }

    public String blockHandler(BlockException ex){
        //ex.printStackTrace();
        System.out.println("blocked");
        return "blocked";
    }

    public static void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("hello");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(1);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

}
