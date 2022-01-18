package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.BotCommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import java.lang.Boolean;
import java.lang.ClassCastException;
import java.lang.Deprecated;
import java.lang.Double;
import java.lang.NullPointerException;
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

public final class RowToHeadersCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(RowToHeadersCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.entrySet().stream().filter(en -> !Arrays.asList( new String[] {}).contains(en.getKey()) && en.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    RowToHeaders command = new RowToHeaders();
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

    if(parameters.containsKey("rowIdx") && parameters.get("rowIdx") != null && parameters.get("rowIdx").get() != null) {
      convertedParameters.put("rowIdx", parameters.get("rowIdx").get());
      if(convertedParameters.get("rowIdx") !=null && !(convertedParameters.get("rowIdx") instanceof Double)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","rowIdx", "Double", parameters.get("rowIdx").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("rowIdx") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","rowIdx"));
    }
    if(convertedParameters.containsKey("rowIdx")) {
      try {
        if(convertedParameters.get("rowIdx") != null && !((double)convertedParameters.get("rowIdx") >= 0)) {
          throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.GreaterThanEqualTo","rowIdx", "0"));
        }
      }
      catch(ClassCastException e) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","rowIdx", "Number", convertedParameters.get("rowIdx").getClass().getSimpleName()));
      }
      catch(NullPointerException e) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","rowIdx"));
      }

    }
    if(parameters.containsKey("deleteRow") && parameters.get("deleteRow") != null && parameters.get("deleteRow").get() != null) {
      convertedParameters.put("deleteRow", parameters.get("deleteRow").get());
      if(convertedParameters.get("deleteRow") !=null && !(convertedParameters.get("deleteRow") instanceof Boolean)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","deleteRow", "Boolean", parameters.get("deleteRow").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("deleteRow") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","deleteRow"));
    }

    try {
      Optional<Value> result =  Optional.ofNullable(command.action((Table)convertedParameters.get("Tabela"),(Double)convertedParameters.get("rowIdx"),(Boolean)convertedParameters.get("deleteRow")));
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
