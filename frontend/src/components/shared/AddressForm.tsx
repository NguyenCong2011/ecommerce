import { useEffect, useState } from "react";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "../ui/form";
import RenderFormField from "./RenderFormField";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../ui/select";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import axios from "axios";
import { Button } from "../ui/button";
import { addressSchema, AddressType } from "@/zod";

type AddressFormProps = {
  onSubmit: (data: any) => void;
  defaultValues?: UserAddress;
  isPending: boolean;
};

const AddressForm = ({
  onSubmit,
  defaultValues,
  isPending,
}: AddressFormProps) => {
  const [provinceData, setProvinceData] = useState<any[]>([]);
  const [districtData, setDistrictData] = useState<any[]>([]);
  const [wardData, setWardData] = useState<any[]>([]);

  const [provinceCode, setProvinceCode] = useState<string | null>(null);
  const [districtCode, setDistrictCode] = useState<string | null>(null);

  // ==================== PROVINCE ====================
  useEffect(() => {
    const fetchProvinces = async () => {
      const response = await axios.get(
        "https://provinces.open-api.vn/api/v1/p/"
      );
      setProvinceData(response.data);
    };
    fetchProvinces();
  }, []);

  // ==================== DISTRICT ====================
  useEffect(() => {
    if (!provinceCode) return;

    const fetchDistricts = async () => {
      const response = await axios.get(
        `https://provinces.open-api.vn/api/v1/p/${provinceCode}?depth=2`
      );
      setDistrictData(response.data.districts || []);
    };

    fetchDistricts();
  }, [provinceCode]);

  // ==================== WARD ====================
  useEffect(() => {
    if (!districtCode) return;

    const fetchWards = async () => {
      const response = await axios.get(
        `https://provinces.open-api.vn/api/v1/d/${districtCode}?depth=2`
      );
      setWardData(response.data.wards || []);
    };

    fetchWards();
  }, [districtCode]);

  const form = useForm<AddressType>({
    resolver: zodResolver(addressSchema),
    defaultValues: defaultValues,
  });

  useEffect(() => {
    if (defaultValues?.province) {
      setProvinceCode(defaultValues.province.split("-")[0]);
    }
    if (defaultValues?.district) {
      setDistrictCode(defaultValues.district.split("-")[0]);
    }
  }, [defaultValues?.province, defaultValues?.district]);

  return (
    <Form {...form}>
      <form className="space-y-3" onSubmit={form.handleSubmit(onSubmit)}>
        <RenderFormField
          title="Full Name"
          name="fullName"
          control={form.control}
          type="input"
        />
        <RenderFormField
          title="Phone Number"
          name="phoneNumber"
          control={form.control}
          type="input"
        />

        {/* ==================== PROVINCE ==================== */}
        <FormField
          control={form.control}
          name="province"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Province</FormLabel>
              <Select
                onValueChange={(value) => {
                  const [code] = value.split("-");
                  setProvinceCode(code);
                  field.onChange(value);

                  setDistrictData([]);
                  setWardData([]);
                  setDistrictCode(null);

                  form.setValue("district", undefined);
                  form.setValue("ward", undefined);
                }}
                defaultValue={field.value}
              >
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="Select a province" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent className="max-h-[315px]">
                  {provinceData.map((province) => (
                    <SelectItem
                      key={province.code}
                      value={`${province.code}-${province.name}`}
                    >
                      {province.name}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <FormMessage />
            </FormItem>
          )}
        />

        {/* ==================== DISTRICT ==================== */}
        <FormField
          control={form.control}
          name="district"
          render={({ field }) => (
            <FormItem>
              <FormLabel>District</FormLabel>
              <Select
                onValueChange={(value) => {
                  const [code] = value.split("-");
                  setDistrictCode(code);
                  field.onChange(value);

                  setWardData([]);
                  form.setValue("ward", undefined);
                }}
                defaultValue={field.value}
              >
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="Select a district" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  {districtData.map((district) => (
                    <SelectItem
                      key={district.code}
                      value={`${district.code}-${district.name}`}
                    >
                      {district.name}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <FormMessage />
            </FormItem>
          )}
        />

        {/* ==================== WARD ==================== */}
        <FormField
          control={form.control}
          name="ward"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Ward</FormLabel>
              <Select onValueChange={field.onChange} defaultValue={field.value}>
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="Select a ward" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  {wardData.map((ward) => (
                    <SelectItem key={ward.code} value={ward.name}>
                      {ward.name}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <FormMessage />
            </FormItem>
          )}
        />

        <RenderFormField
          title="Specify"
          name="specify"
          control={form.control}
          type="input"
        />

        <div className="flex justify-end items-center">
          <Button disabled={isPending}>Submit</Button>
        </div>
      </form>
    </Form>
  );
};

export default AddressForm;
