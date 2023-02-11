package br.ufs.dcomp.ExemploRabbitMQ;

import com.rabbitmq.client.*;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.io.IOException;

public class ChatRabbit{
    public static String QUEUE_NAME;
    public static void main(String[] argv) throws Exception {
        Scanner leitor = new Scanner(System.in);  
        
        SimpleDateFormat df = new SimpleDateFormat("(dd/MM/yyyy) 'às' (HH:mm) ");
        df.setTimeZone(TimeZone.getTimeZone("GMT-3"));
        Date date = new Date();
        String timeStamp = df.format(date);
        
        System.out.println("User: ");
        String userName = leitor.nextLine();
        QUEUE_NAME = userName;
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("100.26.172.133");  //alter aleatoriamente, verificar no EC2
        factory.setUsername("gabrielclf");  
        factory.setPassword("curaga");  
        factory.setVirtualHost("/");    
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
                           //(queue-name, durable, exclusive, auto-delete, params); 
        channel.queueDeclare(QUEUE_NAME, false,   false,     false,       null);
        
        //Receptor
        Consumer consumer = new DefaultConsumer(channel) {
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

            String msg = new String(body, "UTF-8");
            
            System.out.println("\n"+timeStamp+" "+QUEUE_NAME+" diz: "+msg+ "\n");
            System.out.print("@"+QUEUE_NAME+">> ");
                        //(deliveryTag,               multiple);
            //channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
                          //(queue-name, autoAck, consumer);    
        channel.basicConsume(QUEUE_NAME, true,    consumer);
        
        String mensagem = "";
        String prompt = "";
       while (true){
           //int check = 0;
           //Seguro Remover aqui?
           System.out.print(prompt+">> ");
            mensagem = leitor.nextLine();
            if (mensagem == "sair"){ break; }
            
            if (mensagem.charAt(0) == '@'){ 
                QUEUE_NAME = mensagem.substring(1,mensagem.length());
                prompt = mensagem;
                System.out.print(prompt+">> ");
                mensagem = leitor.nextLine();
                                //  (exchange, routingKey, props, message-body   
                channel.basicPublish("",       QUEUE_NAME, null,  mensagem.getBytes("UTF-8"));
                
            }
                //  (exchange, routingKey, props, message-body   
                channel.basicPublish("",       QUEUE_NAME, null,  mensagem.getBytes("UTF-8"));
               //check = 1;
        } 
        channel.close();
        connection.close();
    }
}