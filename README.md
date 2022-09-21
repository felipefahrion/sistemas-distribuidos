# Como rodar

- Ter Java instalado e configurado.


```
make
```

Rodar o servidor: 

```
java p2pServer <ip_address>
```

Por default a execução do servidor já ocorre na porta 9000

Para rodar um peer: 

- Criar: 

```
java p2pPeer 127.0.0.1 "create <peer_name>" <port>
```

- Listar: 

```
java p2pPeer 127.0.0.1 "list <peer_name>" <port>
```