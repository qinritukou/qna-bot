package com.infosupport.demos.qnabot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.aad.adal4j.AuthenticationException;
import com.microsoft.bot.connector.ConnectorClient;
import com.microsoft.bot.connector.customizations.CredentialProvider;
import com.microsoft.bot.connector.customizations.CredentialProviderImpl;
import com.microsoft.bot.connector.customizations.JwtTokenValidation;
import com.microsoft.bot.connector.customizations.MicrosoftAppCredentials;
import com.microsoft.bot.connector.implementation.ConnectorClientImpl;
import com.microsoft.bot.schema.models.Activity;
import com.microsoft.rest.credentials.ServiceClientCredentials;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatBotServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(ChatBotServlet.class.getName());

    private final ObjectMapper objectMapper;
    private final CredentialProvider credentialProvider;
    private final ServiceClientCredentials clientCredentials;
    private final ChatBot bot;

    public ChatBotServlet() {
        this.credentialProvider = new CredentialProviderImpl(getAppId(), getKey());
        this.objectMapper = ObjectMapperFactory.createObjectMapper();
        this.clientCredentials = new MicrosoftAppCredentials(getAppId(), getKey());
        this.bot = new ChatBotImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            Activity activity = deserializeActivity(request);

            JwtTokenValidation.assertValidActivity(activity, authorizationHeader, credentialProvider);

            ConnectorClient connectorInstance = new ConnectorClientImpl(activity.serviceUrl(), clientCredentials);
            ConversationContext context = new ConversationContextImpl(connectorInstance, activity);

            bot.handle(context);

            response.setStatus(202);
            response.setContentLength(0);
        } catch (AuthenticationException ex) {
            logger.log(Level.WARNING, "User is not authenticated", ex);
            writeJsonResponse(response, 401, new ApplicationError("Unauthenticated request"));
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Failed to process incoming activity", ex);
            writeJsonResponse(response, 500, new ApplicationError("Failed to process request"));
        }
    }

    private void writeJsonResponse(HttpServletResponse response, int statusCode, Object value) {
        response.setContentType("application/json");
        response.setStatus(statusCode);

        try (PrintWriter writer = response.getWriter()) {
            objectMapper.writeValue(writer, value);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Failed to serialize object to output stream", ex);
        }
    }

    private Activity deserializeActivity(HttpServletRequest request) throws IOException {
        return objectMapper.readValue(request.getReader(), Activity.class);
    }

    private String getAppId() {
        String appId = System.getenv("BOT_APPID");

        if (appId == null) {
            return "";
        }

        return appId;
    }

    private String getKey() {
        String key = System.getenv("BOT_KEY");

        if (key == null) {
            return "";
        }

        return key;
    }
}
