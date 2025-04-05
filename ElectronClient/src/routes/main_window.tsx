import { Box, Button, TextField } from "@mui/material";
//import { useState } from "react";
import $ from 'jquery'



function call() {
    $.ajax({
        url: "http://localhost:1080",
        data:{
          key: "test"
        },
        success: function( result ) {
          alert(result);
        }
      }); 
    
} 

export default function MainWindow() {
    //const [data, setData] = useState("");
    return (
        <Box>
            <TextField label="test" variant="filled" />
            <Button onClick={call}>Call</Button>
        </Box>
    )
}