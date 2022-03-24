package com.automationanywhere.botcommand.samples.commands.conditionals;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.Condition;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import java.lang.Boolean;
import java.lang.ClassCastException;
import java.lang.Object;
import java.lang.String;
import java.lang.Throwable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class HasAllHeadersCondition implements Condition {
  private static final Logger logger = LogManager.getLogger(HasAllHeadersCondition.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  public boolean test(Map<String, Value> parameters) {
    return test(null, parameters, null);
  }

  public boolean test(GlobalSessionContext globalSessionContext, Map<String, Value> parameters,
      Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.entrySet().stream().filter(en -> !Arrays.asList( new String[] {}).contains(en.getKey()) && en.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    HasAllHeaders command = new HasAllHeaders();
    HashMap<String, Object> convertedParameters = new HashMap<String, Object>();
    if(parameters.containsKey("Tabela") && parameters.get("Tabela") != null && parameters.get("Tabela").get() != null) {
      convertedParameters.put("Tabela", parameters.get("Tabela").get());
      if(convertedParameters.get("Tabela") !=null && !(convertedParameters.get("Tabela") instanceof Table)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","Tabela", "Table", parameters.get("Tabela").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("Tabela") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","Tabela"));
    }

    if(parameters.containsKey("list") && parameters.get("list") != null && parameters.get("list").get() != null) {
      convertedParameters.put("list", parameters.get("list").get());
      if(convertedParameters.get("list") !=null && !(convertedParameters.get("list") instanceof List)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","list", "List", parameters.get("list").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("list") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","list"));
    }

    if(parameters.containsKey("btrim") && parameters.get("btrim") != null && parameters.get("btrim").get() != null) {
      convertedParameters.put("btrim", parameters.get("btrim").get());
      if(convertedParameters.get("btrim") !=null && !(convertedParameters.get("btrim") instanceof Boolean)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","btrim", "Boolean", parameters.get("btrim").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("btrim") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","btrim"));
    }

    try {
      boolean result = command.validate((Table)convertedParameters.get("Tabela"),(List<Value>)convertedParameters.get("list"),(Boolean)convertedParameters.get("btrim"));
      return logger.traceExit(result);
    }
    catch (ClassCastException e) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.IllegalParameters","validate"));
    }
    catch (BotCommandException e) {
      logger.fatal(e.getMessage(), e);
      throw e;
    }
    catch (Throwable e) {
      logger.fatal(e.getMessage(),e);
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.NotBotCommandException",e.getMessage()),e);
    }
  }
}
