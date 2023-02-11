package br.ufs.dcomp.ExemploRabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Emissor {

  private final static String QUEUE_NAME = "minha-fila";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("54.237.87.127"); // Alterar 
    factory.setUsername("gabrielclf"); // Alterar 
    factory.setPassword("curaga"); // Alterar 
    factory.setVirtualHost("/");    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

                      //(queue-name, durable, exclusive, auto-delete, params); 
    channel.queueDeclare(QUEUE_NAME, false,   false,     false,       null);

    String message = "Péngyǒu shì yīgè jiānrèn bù bá de jìlùpiàn, zài xiānggǎng zhè zuò chéngshì de shèzhì. Zhǔyǎn: Qiándélè suǒluósī fùbósī 1 ruìqiū mònīkǎ hé yīxiē qítā tāmāde yǎnyuán";
    
                    //  (exchange, routingKey, props, message-body             ); 
    channel.basicPublish("",       QUEUE_NAME, null,  message.getBytes("UTF-8"));
    System.out.println(" [x] Mensagem enviada: '" + message + "'");

    channel.close();
    connection.close();
  }
}