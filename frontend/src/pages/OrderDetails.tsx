import OrderTracking from "@/components/shared/OrderTracking";
import useFetchData from "@/hooks/useFetchData";
import useSetTitle from "@/hooks/useSetTitle";
import { format } from "date-fns";
import React from "react";
import { useParams } from "react-router-dom";

const OrderDetails = () => {
  useSetTitle("Order Details");

  const { id } = useParams();
  const {
    data: order,
    isLoading,
    isError,
  } = useFetchData(`order/${id}`, "", "private");

  if (isLoading) {
    return <div>Loading...</div>;
  }
  if (isError) {
    return <div>An error occurred</div>;
  }

  // 🔥 FUNCTION GỌI API MOMO
  const handleMomoPayment = async () => {
    try {
      const res = await fetch(
        `http://localhost:8080/api/momo/create/${order.id}`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
        }
      );

      const data = await res.json();

      // Nếu backend trả payUrl → Redirect qua MoMo
      if (data.payUrl) {
        window.location.href = data.payUrl;
      } else {
        alert("Không tạo được liên kết thanh toán MoMo");
      }
    } catch (error) {
      console.error(error);
      alert("Lỗi khi gọi API thanh toán MoMo");
    }
  };

  return (
    <section className="py-7">
      <div className="max-w-6xl w-full mx-auto px-4">
        <h2 className="text-transparent mb-8 text-3xl text-center uppercase bg-clip-text bg-gradient-to-b py-2 from-slate-400 to-indigo-600">
          Order {id}
        </h2>

        {/* TRACKING */}
        <OrderTracking status={order.status} />

        {/* 🔥 NÚT THANH TOÁN MOMO — Chỉ hiện khi Pending */}
        {order.status === "Pending" && (
          <button
            onClick={handleMomoPayment}
            className="mt-6 px-6 py-3 bg-pink-500 text-white rounded-lg hover:bg-pink-600 transition"
          >
            Thanh toán MoMo
          </button>
        )}

        <div className="mt-7 space-y-7">
          <p className="text-muted-foreground">
            {format(new Date(order.createdAt), "HH:mm dd/MM/yyyy")}
          </p>

          {/* Address */}
          <div className="text-muted-foreground dark:text-white space-y-5 mt-3">
            <h2 className="text-slate-950 dark:text-white">
              Recieve Address :
            </h2>
            <blockquote className="pl-3 border-l dark:border-white">
              {order.address.specify} - {order.address.phoneNumber}
            </blockquote>
            <p className="text-sm">
              {order.address.province.split("-")[1]},
              {" "}{order.address.district.split("-")[1]},
              {" "}{order.address.ward}
            </p>
          </div>

          {/* Order items */}
          <div className="border px-3 py-2 rounded-sm">
            {order.orderDetails.map((detail: OrderDetails) => (
              <div key={detail.id} className="flex items-center space-x-4 py-2">
                <img
                  src={detail.productItem.avatar}
                  alt={detail.productItem.productName}
                  className="size-24 object-cover aspect-square rounded-md"
                />
                <div className="space-y-2">
                  <p className="text-slate-900 dark:text-white text-lg">
                    {detail.productItem.productName}
                  </p>
                  <p className="text-sm font-semibold">
                    {detail.quantity} x{" "}
                    <span className="text-red-400 text-lg">
                      ${detail.productItem.productPrice}
                    </span>
                  </p>

                  <p className="text-black dark:text-white ">
                    {detail.productItem.productSize.value} -{" "}
                    {detail.productItem.color}
                  </p>
                </div>
              </div>
            ))}
          </div>

        </div>
      </div>
    </section>
  );
};

export default OrderDetails;
