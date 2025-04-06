import { AppBar, Box, Button, capitalize, colors, List, ListItem, ListItemText, TextField, Typography,  } from "@mui/material";
import { ReactNode, useState } from "react";
import $   from 'jquery';


export default function MainWindow() {
    const [data, setData] = useState({name:"" ,summary:"",description:"",diseases:[""]});

    const [drugs, setDrugs] = useState<{ gene: "", indication: "", score: number }[]>([]);
    
    if(data.name=="")
      setData(JSON.parse('{"summary":"This gene encodes a tumor suppressor protein containing transcriptional activation, DNA binding, and oligomerization domains","description":"tumor protein p53","diseases":["BONE MARROW FAILURE SYNDROME 5; BMFS5","BASAL CELL CARCINOMA, SUSCEPTIBILITY TO, 7; BCC7","NASOPHARYNGEAL CARCINOMA","PAPILLOMA OF CHOROID PLEXUS; CPP","PANCREATIC CANCER","OSTEOGENIC SARCOMA","ADRENOCORTICAL CARCINOMA, HEREDITARY; ADCC","TUMOR PROTEIN p53; TP53","LI-FRAUMENI SYNDROME; LFS","GLIOMA SUSCEPTIBILITY 1; GLM1"],"name":"TP53"}'))
    // console.log('test' + data.diseases)
    const list:ReactNode[] = []
    if("diseases" in data)
      data.diseases.forEach((value,index) => {
        list.push(<ListItem disablePadding key={index}>
          {/* <ListItemIcon>
            <CircleIcon fontSize="small"/>
          </ListItemIcon> */}
          <ListItemText primary={value} sx={{ pl: "10px" }}/>
        </ListItem>)
      })
    
    return (
      <>
        <Box sx={{margin:"10px",backgroundColor:colors.grey[300], justifyContent:"left", display:"flex", flexDirection:"row", alignContent:"center", borderRadius:"20px"}}>
            <TextField label="Gene" variant="filled" id='searchTextField' sx={{marginLeft:"30px", backgroundColor:"transparent", borderRadius:"20px"}}
            onKeyUp={(event) => {
              if (event.keyCode === 13){
                (document.getElementById("button")).click()
              }
            }}  />
            <Button id="button" variant="outlined" sx={{margin:"5px", border:"1px solid black"}}
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
            }}><Typography variant="body2">
                Explore
              </Typography>
            </Button>
        </Box>
        <Box sx={{minWidth:"100px", minHeight:"100px", flexDirection:"row", display:"flex"}}>
          <Box sx={{flexDirection: "column", flexGrow: 1, maxWidth:"25%", backgroundColor:colors.grey[300], margin:"25px", borderRadius:"25px", padding:"20px"}}>
            <div>
              <Typography variant="h3" sx={{textAlign:"center", borderBottom:"4px solid black", marginBottom:"15px"}}>
                {data.name}
              </Typography>
              
              <Typography variant="h6" sx={{textTransform:"capitalize"}}>
                {data.description}
              </Typography>
              <Typography variant="body1" sx={{marginTop:"10px", marginBottom:"15px", fontStyle:"italic"}}>
                {data.summary}
              </Typography> 
              <Typography variant="h6" sx={{fontWeight:"bold", textDecoration:"underline", textDecorationThickness:"2px"}}>
                Diseases:
              </Typography>
              <List>
                {list}
              </List>
            </div>
          </Box>
          <Box sx={{flexGrow:2, margin:"25px", backgroundColor:colors.grey[300], borderRadius:"25px", padding:"20px"}}>
            <Typography variant="h3" sx={{textAlign:"center", borderBottom:"4px solid black", marginBottom:"15px"}}>
              test
            </Typography>
          </Box>
          <Box sx={{flexGrow:1, margin:"25px", backgroundColor:colors.grey[300], borderRadius:"25px", padding:"20px"}}>
  <Typography variant="h4" sx={{textAlign:"center", borderBottom:"4px solid black", marginBottom:"15px"}}>
    Drug Associations
  </Typography>
  <List>
    {drugs.map((drugs, index) => (
      <ListItem key={index} sx={{flexDirection: "column", alignItems: "flex-start", mb: 2}}>
        <Typography variant="subtitle1" sx={{fontWeight: "bold"}}>Gene: {drugs.gene}</Typography>
        <Typography variant="body2">Indication: {drugs.indication}</Typography>
        <Typography variant="body2">Score: {drugs.score}</Typography>
      </ListItem>
    ))}
  </List>
 </Box>

          </Box>
      </>
    )
} 