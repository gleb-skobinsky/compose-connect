const uuid = require("uuid");

const WebSocket = require("ws");

const wss = new WebSocket.Server({ host: "127.0.0.1", port: 8082 });

wss.on("connection", (ws) => {
  ws.id = uuid.v4();
  console.log(`New client connected with id: ${ws.id}`);

  ws.onmessage = ({ data }) => {
    console.log(`Client ${ws.id}: ${data}`);
    wss.clients.forEach(function each(client) {
      if (client !== ws && client.readyState === WebSocket.OPEN) {
        client.send(`${data}`);
      }
    });
  };

  ws.onclose = function () {
    console.log(`Client ${ws.id} has disconnected!`);
  };
});
