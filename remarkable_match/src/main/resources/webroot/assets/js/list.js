
function sendReqRoomList(socket) {
    var builder = new flatbuffers.Builder(1);

    com.clue.fbs.ReqRoomList.startReqRoomList(builder);
    com.clue.fbs.ReqRoomList.addPageCount(builder, 100);
    var packet = com.clue.fbs.ReqRoomList.endReqRoomList(builder);
    builder.finish(packet);
    var buf = builder.asUint8Array();

    var data = new Uint8Array(buf.byteLength + 1);
    data[0] = com.clue.fbs.MessageType.ReqRoomList;
    data.set(buf, 1);
    socket.send(data);
}

function getLeagueComponent(league) {
    switch (league) {
        case 0: return "bronze";
        case 1: return "silver";
        case 2: return "gold";
    }
    return "";
}

function parseRoomList(data) {
    try {
        var buffer = new Uint8Array(data, 0, 1);
        if (buffer[0] != com.clue.fbs.MessageType.ResRoomList) {
            console.log(buffer[0]);
            return null;
        }

        var buf = new flatbuffers.ByteBuffer(new Int8Array(data, 1, data.byteLength-1));
        return com.clue.fbs.ResRoomList.getRootAsResRoomList(buf);
    } catch (err) {
        console.log(err);
    }
}

function main() {
    var socket = new WebSocket("ws://" + location.hostname + ":8020/match");
    socket.binaryType = "arraybuffer";

    socket.onopen = function() {
      console.log("onopen");
      sendReqRoomList(socket);
    };

    socket.onclose = function() {
    };

    socket.onmessage = function(evt) {
        console.log("on message");
        var roomList = parseRoomList(evt.data);
        if (roomList == null) {
            return;
        }

        for (i=0; i<roomList.roomSetLength(); i++) {
            var roomSet = roomList.roomSet(i);
            var leagueComponent = getLeagueComponent(roomSet.league());
            var desc = $('#'+leagueComponent+'-desc');
            var ul = $('#'+leagueComponent);
            desc.empty();
            ul.empty();

            desc.append('open:' + roomSet.openRoomCount() + ' play:' + roomSet.playRoomCount());

            for (index=0; index<roomSet.openRoomsLength(); index++) {
                var room = roomSet.openRooms(index);
                ul.append($('<li class="list-group-item list-group-item-info">').html("<a href='../log/"+room.roomId()+"'>"+room.roomId()+"</a>"));
            }

            for (index=0; index<roomSet.playRoomsLength(); index++) {
                var room = roomSet.playRooms(index);
                ul.append($('<li class="list-group-item">').html("<a href='../log/"+room.roomId()+"'>"+room.roomId()+"</a>"));
            }
        }

        setTimeout(function(){ sendReqRoomList(socket); }, 2000);
    };
}

main();
