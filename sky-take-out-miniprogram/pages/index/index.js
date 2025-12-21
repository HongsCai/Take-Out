// index.js
const defaultAvatarUrl = 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0'

Page({
  data: {
    avatarUrl: defaultAvatarUrl,
    nickname: '',
    code: ''
  },

  // 1. 处理选择头像
  onChooseAvatar(e) {
    const { avatarUrl } = e.detail 
    console.log('用户选择的头像临时路径：', avatarUrl)
    
    // 注意：这里的 avatarUrl 只是临时路径，实际开发中需要上传到服务器
    this.setData({
      avatarUrl
    })
  },

  // 2. 处理昵称输入/选择
  // 微信键盘上方会自动提示微信昵称，用户点击后会触发此事件
  onNicknameChange(e) {
    const nickname = e.detail.value
    console.log('用户输入的昵称：', nickname)
    this.setData({
      nickname
    })
  },

  // 3. 提交注册
  onSubmit() {
    const { avatarUrl, nickname } = this.data;
    if (!nickname) {
      wx.showToast({ title: '请输入昵称', icon: 'none' });
      return;
    }
    
    // TODO: 这里写调用后端注册接口的逻辑
    console.log('提交注册信息', { avatarUrl, nickname });
    wx.showToast({ title: '注册成功' });
  },

  onLogin() {
    wx.login({
      success: (res) => {
        console.log(res.code)
        const {code} = res
        this.setData({
          code
        })
      }
    })
  },

  sendRequest() {
    wx.request({
      url: 'http://localhost:8080/user/shop/status',
      method: 'GET',
      success: (res) => {
        console.log(res.data)
      }
    })
  }


})