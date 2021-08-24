package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.BotCommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import java.lang.ClassCastException;
import java.lang.Deprecated;
import java.lang.Object;
import java.lang.String;
import java.lang.Throwable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class CalculateCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(CalculateCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.entrySet().stream().filter(en -> !Arrays.asList( new String[] {}).contains(en.getKey()) && en.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    Calculate command = new Calculate();
    HashMap<String, Object> convertedParameters = new HashMap<String, Object>();
    if(parameters.containsKey("i_tabela") && parameters.get("i_tabela") != null && parameters.get("i_tabela").get() != null) {
      convertedParameters.put("i_tabela", parameters.get("i_tabela").get());
      if(convertedParameters.get("i_tabela") !=null && !(convertedParameters.get("i_tabela") instanceof Table)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","i_tabela", "Table", parameters.get("i_tabela").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("i_tabela") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","i_tabela"));
    }

    if(parameters.containsKey("i_colunasCalc") && parameters.get("i_colunasCalc") != null && parameters.get("i_colunasCalc").get() != null) {
      convertedParameters.put("i_colunasCalc", parameters.get("i_colunasCalc").get());
      if(convertedParameters.get("i_colunasCalc") !=null && !(convertedParameters.get("i_colunasCalc") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","i_colunasCalc", "String", parameters.get("i_colunasCalc").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("i_colunasCalc") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","i_colunasCalc"));
    }

    if(parameters.containsKey("i_code") && parameters.get("i_code") != null && parameters.get("i_code").get() != null) {
      convertedParameters.put("i_code", parameters.get("i_code").get());
      if(convertedParameters.get("i_code") !=null && !(convertedParameters.get("i_code") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","i_code", "String", parameters.get("i_code").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("i_code") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","i_code"));
    }

    try {
      Optional<Value> result =  Optional.ofNullable(command.action((Table)convertedParameters.get("i_tabela"),(String)convertedParameters.get("i_colunasCalc"),(String)convertedParameters.get("i_code")));
      return logger.traceExit(result);
    }
    catch (ClassCastException e) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.IllegalParameters","action"));
    }
    catch (BotCommandException e) {
      logger.fatal(e.getMessage(),e);
      throw e;
    }
    catch (Throwable e) {
      logger.fatal(e.getMessage(),e);
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.NotBotCommandException",e.getMessage()),e);
    }
  }
}
