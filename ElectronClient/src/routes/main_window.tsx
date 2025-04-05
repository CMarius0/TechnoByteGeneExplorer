import { Box, Button, TextField,  } from "@mui/material";

import { useState } from "react";
import $ from 'jquery';


export default function MainWindow() {
    const [data, setData] = useState({name:"" ,summary:""});
    return (
      <>
        <Box>
            <TextField label="Gene" variant="filled" id='searchTextField' />
            <Button variant="contained"
              onClick={() => {
              $.ajax({
                url: "http://localhost:1080",
                data:{
                  type: "GetGene",
                  gene: (document.getElementById("searchTextField") as HTMLInputElement).value
                },
                success: function( result ) {
                  const gene = JSON.parse(result);
                  setData(gene);
                }
              });
            }}>Search</Button>
        </Box>
        <Box sx={{flexDirection: "column"}}>
          <div>
            <p>{data.name}</p>
            <p>{data.summary}</p>
          </div>
        </Box>
      </>
    )
} 