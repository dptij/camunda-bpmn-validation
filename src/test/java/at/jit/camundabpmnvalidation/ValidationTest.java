package at.jit.camundabpmnvalidation;

import org.apache.commons.io.FileUtils;
import org.camunda.bpm.engine.ParseException;
import org.camunda.bpm.engine.impl.cfg.BpmnParseFactory;
import org.camunda.bpm.engine.impl.cfg.DefaultBpmnParseFactory;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.interceptor.CommandInterceptor;
import org.camunda.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.junit.Assert;
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
        validateFile("src/test/resources/diagram_1.bpmn");
    }

    private void validateFile(String fileName) throws IOException {
        try (FileInputStream inputStream = FileUtils.openInputStream(new File(fileName))) {
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
        }
        catch (final ParseException exception) {
            exception.printStackTrace();
            Assert.fail();
        }
    }
}
