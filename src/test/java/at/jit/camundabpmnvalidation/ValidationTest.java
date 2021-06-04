package at.jit.camundabpmnvalidation;

import org.apache.commons.io.FileUtils;
import org.camunda.bpm.engine.ParseException;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParse;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParser;
import org.camunda.bpm.engine.impl.cfg.BpmnParseFactory;
import org.camunda.bpm.engine.impl.cfg.DefaultBpmnParseFactory;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.form.type.BooleanFormType;
import org.camunda.bpm.engine.impl.form.type.FormTypes;
import org.camunda.bpm.engine.impl.form.type.LongFormType;
import org.camunda.bpm.engine.impl.form.type.StringFormType;
import org.camunda.bpm.engine.impl.interceptor.CommandInterceptor;
import org.camunda.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;

public class ValidationTest {

    @Test(expected = AssertionError.class)
    public void diagram_1() throws IOException {
        validateFile("src/test/resources/diagram_1.bpmn");
    }

    @Test
    public void lesson12_scenes() throws IOException {
        validateFile("src/test/resources/lesson12_scenes.bpmn");
    }

    @Test
    public void MultiSessionWritingTaskProcess() throws IOException {
        validateFile("src/test/resources/MultiSessionWritingTaskProcess.bpmn");
    }

    @Test
    public void publishToDpisarenkoComProcess() throws IOException {
        validateFile("src/test/resources/publishToDpisarenkoComProcess.bpmn");
    }

    @Test
    public void researchForWritingProjectProcess() throws IOException {
        validateFile("src/test/resources/ResearchForWritingProjectProcess.bpmn");
    }

    private void validateFile(String fileName) throws IOException {
        try (FileInputStream inputStream = FileUtils.openInputStream(new File(fileName))) {
            final ExpressionManager testExpressionManager = new ExpressionManager();

            ProcessEngineConfigurationImpl processEngineConfiguration = new ProcessEngineConfigurationImpl() {
                @Override
                protected Collection<? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequired() {
                    return null;
                }

                @Override
                protected Collection<? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequiresNew() {
                    return null;
                }

                @Override
                public ExpressionManager getExpressionManager() {
                    return testExpressionManager;
                }

                @Override
                public FormTypes getFormTypes() {
                    final FormTypes formTypes = new FormTypes();
                    formTypes.addFormType(new BooleanFormType());
                    formTypes.addFormType(new StringFormType());
                    formTypes.addFormType(new LongFormType());
                    return formTypes;
                }
            };

            Context.setProcessEngineConfiguration(processEngineConfiguration);

            BpmnParseFactory bpmnParseFactory = new DefaultBpmnParseFactory();
            BpmnParser bpmnParser = new BpmnParser(testExpressionManager, bpmnParseFactory);
            BpmnParse bpmnParse = bpmnParser.createParse()
                    .sourceInputStream(inputStream)
                    .deployment(new DeploymentEntity())
                    .name(fileName);
            bpmnParse.execute();
        }
        catch (final ParseException exception) {
            exception.printStackTrace();
            Assert.fail("BPMN is invalid, see log output for details");
        }
    }
}
