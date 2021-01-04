package ru.otus.multiprocess.backend;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.multiprocess.backend.cachehw.HwCache;
import ru.otus.multiprocess.backend.cachehw.MyCache;
import ru.otus.multiprocess.backend.core.model.AddressDataSet;
import ru.otus.multiprocess.backend.core.model.PhoneDataSet;
import ru.otus.multiprocess.backend.core.model.User;
import ru.otus.multiprocess.backend.core.service.DBServiceUser;
import ru.otus.multiprocess.backend.hibernate.HibernateUtils;
import ru.otus.multiprocess.backend.hibernate.sessionmanager.CustomSessionFactory;
import ru.otus.multiprocess.backend.hibernate.sessionmanager.HibernateCustomSessionFactory;
import ru.otus.multiprocess.backend.messaging.CreateUserRequestHandler;
import ru.otus.multiprocess.backend.messaging.GetAllUsersRequestHandler;
import ru.otus.multiprocess.messagesystem.HandlersStore;
import ru.otus.multiprocess.messagesystem.HandlersStoreImpl;
import ru.otus.multiprocess.messagesystem.client.CallbackRegistry;
import ru.otus.multiprocess.messagesystem.client.CallbackRegistryImpl;
import ru.otus.multiprocess.messagesystem.client.MsClient;
import ru.otus.multiprocess.messagesystem.client.MsClientImpl;
import ru.otus.multiprocess.messagesystem.message.MessageType;


@Configuration
@ComponentScan
public class Config {

    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";


    @Bean
    public HwCache<String, User> userHwCache() {
        return new MyCache<>();
    }

    @Bean
    public CustomSessionFactory sessionFactory() {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE,
                User.class, AddressDataSet.class, PhoneDataSet.class);
        return new HibernateCustomSessionFactory(sessionFactory);
    }


    @Bean
    public CallbackRegistry callbackRegistry() {
        return new CallbackRegistryImpl();
    }


    @Bean
    public MsClient databaseMsClient(
            CallbackRegistry callbackRegistry,
            DBServiceUser dbServiceUser,
            @Value("${mysocket.clientName}") String clientName,
            @Value("${mysocket.clientPort}") Integer clientPort,
            @Value("${mysocket.messageSystem.host}") String messageSystemHost,
            @Value("${mysocket.messageSystem.port}") Integer messageSystemPort) {

        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(MessageType.CREATE_USER, new CreateUserRequestHandler(dbServiceUser));
        requestHandlerDatabaseStore.addHandler(MessageType.ALL_USER_DATA, new GetAllUsersRequestHandler(dbServiceUser));

        return new MsClientImpl(clientName,
                messageSystemHost, messageSystemPort, clientPort, requestHandlerDatabaseStore, callbackRegistry);

    }


}
