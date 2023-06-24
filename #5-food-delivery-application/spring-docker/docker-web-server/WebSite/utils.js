var order_url = "localhost:8090";
var shipping_url = "localhost:8092";
var user_url = "localhost:8091";


function getUrlParameters(parameterName){
    var result = null,
    tmp = [];
    location.search
        .substr(1)
        .split("&")
        .forEach(function (item) {
        tmp = item.split("=");
            if (tmp[0] === parameterName) result = decodeURIComponent(tmp[1]);
        });
    return result;
}

function getAvailability(callback) {
    let xmlHttpReq = new XMLHttpRequest();
    xmlHttpReq.onreadystatechange = function () {
      if (xmlHttpReq.readyState == 4 && xmlHttpReq.status == 200){
        callback(xmlHttpReq.responseText);
      }else if(xmlHttpReq.status == 0){
        console.log("Error " + xmlHttpReq.status + ": " + xmlHttpReq.statusText)
      }else{
        alert(`Error: ${xmlHttpReq.status}`);
      }
    };
    xmlHttpReq.open("GET", "http://"+  order_url +"/order/availability", false); // true for asynchronous
    xmlHttpReq.send(null);
}

function updateAvaiability(){
    const item_input = document.getElementById("item");
    const quantity_input = document.getElementById("quantity");

    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("POST", "http://"+  order_url +"/order/availability", false);
    xmlHttp.setRequestHeader("Content-Type", "application/json; charset=UTF-8")
    const body = JSON.stringify({
        itemName: item_input.value,
        amount: quantity_input.value
    });

    console.log(body);
    xmlHttp.onload = () => {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
            console.log(xmlHttp.responseText);
            reloadToTable("orderTable");
        } else {
            alert(`Error: ${xmlHttp.status}`);
        }
    };
    xmlHttp.send(body);
}

function reloadOrderTable(tableName){
    const tableBody = document.querySelector("." + tableName);
    while (tableBody.childNodes[1]) {
        tableBody.removeChild(tableBody.childNodes[1]);
    }

    getAvailability(function (response) {
        let availability = JSON.parse(response);
        addRowToTable(availability, tableName);
    });
}

function createTable(tableDivName, tableHeaders, tableName){
    const tableDiv = document.getElementById(tableDivName);

    const createTable = () => {
        while (tableDiv.firstChild) {
            tableDiv.removeChild(tableDiv.firstChild);
        }
    }

    let table = document.createElement("table");
    table.className = tableName;

    let tableHead = document.createElement("thead");
    tableHead.className = "tableHead";

    let tableHeadRow = document.createElement("tr");
    tableHeadRow.className = "tableHeadRow";

    tableHeaders.forEach(headerText => {
        let header = document.createElement("th");
        header.innerText = headerText;
        tableHeadRow.append(header);
    });

    tableHead.append(tableHeadRow);
    table.append(tableHead);

    let tableBody = document.createElement("tbody");
    tableBody.className = "tableBody";
    table.append(tableBody);

    tableDiv.append(table);
};



function addRowToTable(rowData, tableName){
    rowData.forEach((item) =>{
        const tableBody = document.querySelector("." + tableName);

        let row = document.createElement("tr");
        row.className = "tableBodyRow";

        let cell = document.createElement("td");
        cell.innerText = item.name;
        row.append(cell);

        cell = document.createElement("td");
        cell.innerText = item.amount;
        row.append(cell);

        tableBody.append(row);
    });
};

function addRowToTableOrderStatus(rowData, tableName){
    rowData.forEach((item) =>{
        const tableBody = document.querySelector("." + tableName);

        let row = document.createElement("tr");
        row.className = "tableBodyRow";

        let cell = document.createElement("td");
        cell.innerText = item.id;
        row.append(cell);

        cell = document.createElement("td");
        cell.innerText = item.orderState;
        row.append(cell);

        cell = document.createElement("td");
        let itemsString = "| ";
        for(var key in item.items){
            itemsString += key + ": " + item.items[key] + " | ";
        }
        cell.innerText = itemsString;
        row.append(cell);

        tableBody.append(row);
    });
};