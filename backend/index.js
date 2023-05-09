const WebSocket = require("ws");

const wss = new WebSocket.Server({ host: "127.0.0.1", port: 8082 });

wss.getUniqueID = function () {
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  }
  return s4() + s4() + "-" + s4();
};

wss.on("connection", (ws) => {
  ws.id = wss.getUniqueID();
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
