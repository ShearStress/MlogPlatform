<%@ page contentType="text/html;charset-UTF-8" language="java" isELIgnored="false" pageEncoding="UTF-8"%>
	<div class="col-md-9">



<div class="data_list">
	<div class="data_list_title"><span class="glyphicon glyphicon-edit"></span>&nbsp;个人中心 </div>
	<div class="container-fluid">
	  <div class="row" style="padding-top: 20px;">
	  	<div class="col-md-8">
	  		<form class="form-horizontal" method="post" action="user?act=save" enctype="multipart/form-data">
			  <div class="form-group">
			  	<input type="hidden" name="act" value="save">
			    <label for="nickName" class="col-sm-2 control-label">昵称:</label>
			    <div class="col-sm-3">
			      <input class="form-control" name="nick" id="nickName" placeholder="昵称" value="${user.nick}">
			    </div>
			    <label for="img" class="col-sm-2 control-label">头像:</label>
			    <div class="col-sm-5">
			    	<input type="file" id="img" name="img">
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="mood" class="col-sm-2 control-label">心情:</label>
			    <div class="col-sm-10">
			      <textarea class="form-control" name="mood" id="mood" rows="3">${user.mood}</textarea>
			    </div>
			  </div>
			  <div class="form-group">
			    <div class="col-sm-offset-2 col-sm-10">
			      <button type="submit" id="btn" class="btn btn-success">修改</button>&nbsp;&nbsp;<span style="color:red; font-size: 12px" id="msg"></span>
			    </div>
			  </div>
			</form>
	  	</div>
  		<div class="col-md-4"><img style="width:240px;height:180px" src="user?actionName=userHead&imageName=${user.head}"></div>
	  </div>
	</div>
</div>

</div>


<script type="text/javascript">

    $("#nickName").blur(function (){
        var nickName = $("#nickName").val();

        if (isEmpty(nickName)){
            $("#msg").html("Nick name cannot be empty")
            $("#btn").prop("disabled", true);
            return;
        }

        var nick = '${user.nick}';

        if (nickName == nick){
            return;
        }

        $.ajax({
            type:"get",
            url:"user",
            data:{
                actionName:"checkNick",
                nick:nickName
            },
            success:function(result){
                if (result == 1){
                    $("#msg").html("")
                    $("#btn").prop("disabled", false);
                } else{
                    $("#msg").html("Existed nick name.")
                    $("#btn").prop("disabled", true);
                }
            }
        });

    }).focus(function () {

        $("#msg").html("")
        $("#btn").prop("disabled", false);

    });


</script>