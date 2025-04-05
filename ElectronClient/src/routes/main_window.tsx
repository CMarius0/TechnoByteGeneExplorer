import { Box, Button, colors, TextField, Typography,  } from "@mui/material";
import { useState } from "react";
import $   from 'jquery';



export default function MainWindow() {
    const [data, setData] = useState({name:"" ,summary:"",description:""});
    if(data.name=="")
      setData(JSON.parse('{"summary":"This gene encodes a tumor suppressor protein containing transcriptional activation, DNA binding, and oligomerization domains","description":"tumor protein p53","diseases":["BONE MARROW FAILURE SYNDROME 5; BMFS5","BASAL CELL CARCINOMA, SUSCEPTIBILITY TO, 7; BCC7","NASOPHARYNGEAL CARCINOMA","PAPILLOMA OF CHOROID PLEXUS; CPP","PANCREATIC CANCER","OSTEOGENIC SARCOMA","ADRENOCORTICAL CARCINOMA, HEREDITARY; ADCC","TUMOR PROTEIN p53; TP53","LI-FRAUMENI SYNDROME; LFS","GLIOMA SUSCEPTIBILITY 1; GLM1"],"name":"TP53"}'))
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
                  if ('error' in gene)
                    alert("Error at GetGene")
                  setData(gene);
                }
              });
            }}>Explore</Button>
        </Box>
        <Box sx={{minWidth:"100px", minHeight:"100px", flexDirection:"row", display:"flex"}}>
          <Box sx={{flexDirection: "column", flexGrow: 1, maxWidth:"25%", backgroundColor:colors.grey[300], margin:"25px", borderRadius:"25px", padding:"20px"}}>
            <div>
              <Typography variant="h4">
                {data.name}
              </Typography>
              <Typography variant="h6">
                {data.description}
              </Typography>
              <Typography variant="body2" sx={{marginTop:"10px"}}>
                {data.summary}
              </Typography> 
            </div>
          </Box>
          <Box sx={{flexGrow:2}}>
              test
          </Box>
          <Box sx={{flexGrow:1}}>
              test
          </Box>
        </Box>
      </>
    )
} 