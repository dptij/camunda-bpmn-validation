package at.jit.camundabpmnvalidation;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.cfg.BpmnParseFactory;
import org.camunda.bpm.engine.impl.cfg.DefaultBpmnParseFactory;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.interceptor.CommandInterceptor;
import org.camunda.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.Test;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParser;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParse;
import org.camunda.bpm.engine.impl.context.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;

public class ValidationTest {

    @Test
    public void test1() throws IOException {
        try (FileInputStream inputStream = FileUtils.openInputStream(new File("src/test/resources/diagram_1.bpmn"))) {
            ExpressionManager expressionManager = new ExpressionManager();

            ProcessEngineConfigurationImpl processEngineConfiguration = new ProcessEngineConfigurationImpl() {
                @Override
                protected Collection<? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequired() {
                    return null;
                }

                @Override
                protected Collection<? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequiresNew() {
                    return null;
                }
            };

            Context.setProcessEngineConfiguration(processEngineConfiguration);

            BpmnParseFactory bpmnParseFactory = new DefaultBpmnParseFactory();
            BpmnParser bpmnParser = new BpmnParser(expressionManager, bpmnParseFactory);
            BpmnParse bpmnParse = bpmnParser.createParse()
                    .sourceInputStream(inputStream)
                    .deployment(new DeploymentEntity());
            bpmnParse.execute();
            System.out.println("test");
        }


    }
}
