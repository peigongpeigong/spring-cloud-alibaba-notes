package com.peigong.springcloudalibaba;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: lilei
 * @create: 2020-07-24 14:40
 **/
public class SentinelSample {

    public static void main(String[] args) {
        initFlowsRules();
        while (true) {
            doSomething();
        }
    }

    private static void doSomething(){
        try (Entry entry = SphU.entry("sth.")) {
            System.out.println("Hello " + LocalDateTime.now());
        } catch (BlockException e) {
            System.out.println("Blocked " + LocalDateTime.now());
        }
    }

    private static void initFlowsRules(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("sth.");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(20);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

}
