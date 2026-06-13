package dev.studying;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TwootrTest {

    private static final String USERNAME = "elitest";
    private static final String PASSWORD = "12345";
    private static final String USERNAME_1 = "otherUser";
    private static final String PASSWORD_1 = "password";
    private Twootr twootr;

    @Mock ReceiverEndPoint receiverEndPoint ;
    @Mock ReceiverEndPoint otherReceiverEndPoint ;

    @BeforeEach
    public void setUp(){
        twootr = new Twootr(new InMemoryUserRepository());
    }

    @Test
    public void shouldBeAbleToAuthenticateUser(){
        twootr.registerUser(USERNAME, PASSWORD);
        Optional<SenderEndPoint> result =  twootr.logon(USERNAME,PASSWORD,receiverEndPoint);
        assertTrue(result.isPresent());
    }

    @Test
    public void shouldNotBeAbleToAuthenticateWithWrongPassword(){
        twootr.registerUser(USERNAME, PASSWORD);
        Optional<SenderEndPoint> result = twootr.logon(USERNAME, "wrongPassword",receiverEndPoint);
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldSendTwootsToFollowers(){
        twootr.registerUser(USERNAME, PASSWORD);
        twootr.registerUser(USERNAME_1, PASSWORD_1);

        Optional<SenderEndPoint> result = twootr.logon(USERNAME, PASSWORD,receiverEndPoint);
        Optional<SenderEndPoint> result_1 = twootr.logon(USERNAME_1, PASSWORD_1, otherReceiverEndPoint);

        SenderEndPoint senderEndPoint = result.get();
        SenderEndPoint otherSenderEndPoint = result_1.get();

        otherSenderEndPoint.onFollow(senderEndPoint);

        senderEndPoint.onSendTwoot("1","Hello followers");

        verify(otherReceiverEndPoint).onTwoot(any(Twoot.class));

    }

    @Test
    public void shouldNotSendTwootsToNonFollowers(){
        twootr.registerUser(USERNAME, PASSWORD);

        Optional<SenderEndPoint> result = twootr.logon(USERNAME, PASSWORD,receiverEndPoint);

        SenderEndPoint senderEndPoint = result.get();

        senderEndPoint.onSendTwoot("1","Hello followers");

        verify(otherReceiverEndPoint, never()).onTwoot(any(Twoot.class));
    }

    @Test
    public void shouldReceiveReplayOfTwootsOnLogon(){
        twootr.registerUser(USERNAME, PASSWORD);
        twootr.registerUser(USERNAME_1, PASSWORD_1);

        Optional<SenderEndPoint> result = twootr.logon(USERNAME, PASSWORD,receiverEndPoint);
        Optional<SenderEndPoint> result_1 = twootr.logon(USERNAME_1, PASSWORD_1, otherReceiverEndPoint);

        SenderEndPoint senderEndPoint = result.get();
        SenderEndPoint otherSenderEndPoint = result_1.get();

        otherSenderEndPoint.onFollow(senderEndPoint);

        otherSenderEndPoint.logoff();

        senderEndPoint.onSendTwoot("1", "Hello followers");

        twootr.logon(USERNAME_1, PASSWORD_1, otherReceiverEndPoint);

        verify(otherReceiverEndPoint).onTwoot(any(Twoot.class));

    }




}
