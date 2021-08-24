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

public final class TableQueryCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(TableQueryCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.entrySet().stream().filter(en -> !Arrays.asList( new String[] {}).contains(en.getKey()) && en.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    TableQuery command = new TableQuery();
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

    if(parameters.containsKey("registros") && parameters.get("registros") != null && parameters.get("registros").get() != null) {
      convertedParameters.put("registros", parameters.get("registros").get());
      if(convertedParameters.get("registros") !=null && !(convertedParameters.get("registros") instanceof Double)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","registros", "Double", parameters.get("registros").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("registros") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","registros"));
    }

    if(parameters.containsKey("i_colunas") && parameters.get("i_colunas") != null && parameters.get("i_colunas").get() != null) {
      convertedParameters.put("i_colunas", parameters.get("i_colunas").get());
      if(convertedParameters.get("i_colunas") !=null && !(convertedParameters.get("i_colunas") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","i_colunas", "String", parameters.get("i_colunas").get().getClass().getSimpleName()));
      }
    }

    if(parameters.containsKey("isMysql") && parameters.get("isMysql") != null && parameters.get("isMysql").get() != null) {
      convertedParameters.put("isMysql", parameters.get("isMysql").get());
      if(convertedParameters.get("isMysql") !=null && !(convertedParameters.get("isMysql") instanceof Boolean)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","isMysql", "Boolean", parameters.get("isMysql").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("isMysql") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","isMysql"));
    }

    if(parameters.containsKey("i_colunasUSD") && parameters.get("i_colunasUSD") != null && parameters.get("i_colunasUSD").get() != null) {
      convertedParameters.put("i_colunasUSD", parameters.get("i_colunasUSD").get());
      if(convertedParameters.get("i_colunasUSD") !=null && !(convertedParameters.get("i_colunasUSD") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","i_colunasUSD", "String", parameters.get("i_colunasUSD").get().getClass().getSimpleName()));
      }
    }

    if(parameters.containsKey("i_colunasBRLUSD") && parameters.get("i_colunasBRLUSD") != null && parameters.get("i_colunasBRLUSD").get() != null) {
      convertedParameters.put("i_colunasBRLUSD", parameters.get("i_colunasBRLUSD").get());
      if(convertedParameters.get("i_colunasBRLUSD") !=null && !(convertedParameters.get("i_colunasBRLUSD") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","i_colunasBRLUSD", "String", parameters.get("i_colunasBRLUSD").get().getClass().getSimpleName()));
      }
    }

    try {
      Optional<Value> result =  Optional.ofNullable(command.action((Table)convertedParameters.get("Tabela"),(Double)convertedParameters.get("registros"),(String)convertedParameters.get("i_colunas"),(Boolean)convertedParameters.get("isMysql"),(String)convertedParameters.get("i_colunasUSD"),(String)convertedParameters.get("i_colunasBRLUSD")));
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
