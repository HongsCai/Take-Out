// index.js
Page({
  data: {
    msg: "Hongs_Cai"
  },

  // 获取微信头像和昵称
  getUserInfo() {
    wx.getUserProfile({
      desc: '获取用户信息',
      success: (res) => {
        console.log(res.userInfo)
      }
    })
  }
})
