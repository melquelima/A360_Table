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
import java.lang.Double;
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

public final class SetColumnNameCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(SetColumnNameCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.entrySet().stream().filter(en -> !Arrays.asList( new String[] {}).contains(en.getKey()) && en.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    SetColumnName command = new SetColumnName();
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

    if(parameters.containsKey("TypeIndex") && parameters.get("TypeIndex") != null && parameters.get("TypeIndex").get() != null) {
      convertedParameters.put("TypeIndex", parameters.get("TypeIndex").get());
      if(convertedParameters.get("TypeIndex") !=null && !(convertedParameters.get("TypeIndex") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","TypeIndex", "String", parameters.get("TypeIndex").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("TypeIndex") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","TypeIndex"));
    }
    if(convertedParameters.get("TypeIndex") != null) {
      switch((String)convertedParameters.get("TypeIndex")) {
        case "name" : {
          if(parameters.containsKey("ColumnName") && parameters.get("ColumnName") != null && parameters.get("ColumnName").get() != null) {
            convertedParameters.put("ColumnName", parameters.get("ColumnName").get());
            if(convertedParameters.get("ColumnName") !=null && !(convertedParameters.get("ColumnName") instanceof String)) {
              throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","ColumnName", "String", parameters.get("ColumnName").get().getClass().getSimpleName()));
            }
          }
          if(convertedParameters.get("ColumnName") == null) {
            throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","ColumnName"));
          }


        } break;
        case "index" : {
          if(parameters.containsKey("ColumnIndex") && parameters.get("ColumnIndex") != null && parameters.get("ColumnIndex").get() != null) {
            convertedParameters.put("ColumnIndex", parameters.get("ColumnIndex").get());
            if(convertedParameters.get("ColumnIndex") !=null && !(convertedParameters.get("ColumnIndex") instanceof Double)) {
              throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","ColumnIndex", "Double", parameters.get("ColumnIndex").get().getClass().getSimpleName()));
            }
          }
          if(convertedParameters.get("ColumnIndex") == null) {
            throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","ColumnIndex"));
          }


        } break;
        default : throw new BotCommandException(MESSAGES_GENERIC.getString("generic.InvalidOption","TypeIndex"));
      }
    }

    if(parameters.containsKey("NewColumnName") && parameters.get("NewColumnName") != null && parameters.get("NewColumnName").get() != null) {
      convertedParameters.put("NewColumnName", parameters.get("NewColumnName").get());
      if(convertedParameters.get("NewColumnName") !=null && !(convertedParameters.get("NewColumnName") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","NewColumnName", "String", parameters.get("NewColumnName").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("NewColumnName") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","NewColumnName"));
    }

    try {
      Optional<Value> result =  Optional.ofNullable(command.action((Table)convertedParameters.get("Tabela"),(String)convertedParameters.get("TypeIndex"),(String)convertedParameters.get("ColumnName"),(Double)convertedParameters.get("ColumnIndex"),(String)convertedParameters.get("NewColumnName")));
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
